/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apachecon.memories.speechbubble;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeechBubble {
    private static final Logger LOG = LoggerFactory.getLogger(SpeechBubble.class);
    // TODO: add some colors from the ASF feather palette
    private static final Color[] GRADIENTS = {
    	Color.CYAN, Color.MAGENTA, Color.GREEN, Color.YELLOW
    };
    private static final Random RAND = new Random();
    
    private static final int MAX_WIDTH = 194;
    private static final int MAX_HEIGHT = 130;
    private static final int OUT_PADDING = 8;
    private static final int BUBBLE_WIDTH = MAX_WIDTH - 2 * OUT_PADDING;
    private static final int BUBBLE_HEIGHT = 84;
    private static final int IN_PADDING = 4;
    private static final int ARROW_HEIGHT = 12;
    private static final int ARROW_WIDTH = 8;
    private static final int ARROW_DELTA = 40;
    private static final int ICON_SIZE = 40;
    private static final Font DEF_FONT = new Font(Font.SERIF, Font.PLAIN, 12);

    private Font font;
    private BufferedImage twitterLogo;

    public SpeechBubble() {
        this(MAX_WIDTH, MAX_HEIGHT, DEF_FONT);
    }

    public SpeechBubble(int width, int height, Font font) {
        this.setFont(font != null ? font : DEF_FONT);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }


	public BufferedImage getTwitterLogo() {
		if (twitterLogo == null) {
	        InputStream in = getClass().getResourceAsStream("/img/twitter-logo.png");
	        try {
	        	BufferedImage logo = ImageIO.read(in);

	            twitterLogo = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB);
	            Graphics2D g = twitterLogo.createGraphics();
	            g.drawImage(logo, 0, 0, ICON_SIZE, ICON_SIZE, null);
	            g.dispose();
	        } catch (IOException e) {
	        	// shouldn't happen
	        }
		}
		return twitterLogo;
	}

	public void setTwitterLogo(BufferedImage twitterLogo) {
		this.twitterLogo = twitterLogo;
	}
    /**
     * Format message as a collection of lines to display to fit in a given
     * width
     * 
     * @param text, the message to be displayed
     * @param g2d, the graphics context
     * @param width, the width of the bubble
     * @return array of lines to be displayed
     */
    public List<String> formatText(String text, FontMetrics fm) {
        if (text == null) {
            LOG.warn("No text to format");
            return null;
        }

        int lineCount = BUBBLE_HEIGHT / fm.getHeight();

        List<String> lines = new ArrayList<String>(lineCount);
        String[] tokens = text.split(" ");
        String line = "";
        int lineWidth = 0;

        // Use a dummy Image to calculate FontMetrics
        for (String word : tokens) {
            int w = fm.charsWidth((word + " ").toCharArray(), 0, word.length() + 1);
            lineWidth += w;
            if (lineWidth > BUBBLE_WIDTH) {
                if (lines.size() >= lineCount - 1) {
                    line += "...";
                    break;
                }
                lines.add(line);
                line = "";
                lineWidth = w;
            }
            line += word + " ";
        }
        lines.add(line);
        return lines;
    }

    /**
     * Paint speech bubble
     * 
     * @param lines, the message to be displayed as collection of lines
     * @param g2d, the graphics context
     * @param width, the width of the bubble
     * @return array of lines to be displayed
     */
    public void paintBubble(List<String> lines, Graphics2D graphics) {
        // Add the twitter logo
        BufferedImage logo = getTwitterLogo();
        if (logo != null) {
        	graphics.drawImage(logo, MAX_WIDTH - ICON_SIZE - OUT_PADDING, MAX_HEIGHT - ICON_SIZE, null);
        }

        graphics.setPaint(new GradientPaint(OUT_PADDING, OUT_PADDING, randomGradient(), 
            OUT_PADDING, BUBBLE_HEIGHT - OUT_PADDING, Color.WHITE));
        // First thing is to draw the bubble with a thin black outline
        int arc = IN_PADDING * 4;
        graphics.fillRoundRect(OUT_PADDING, OUT_PADDING, BUBBLE_WIDTH, BUBBLE_HEIGHT, arc, arc);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(OUT_PADDING, OUT_PADDING, BUBBLE_WIDTH, BUBBLE_HEIGHT, arc, arc);

        // With an arror at the bottom
        // TODO: add orientation, maybe
        graphics.setColor(Color.WHITE);
        Point arrowLeft = new Point(OUT_PADDING + BUBBLE_WIDTH - ARROW_DELTA - ARROW_WIDTH, OUT_PADDING + BUBBLE_HEIGHT);
        Point arrowRight = new Point(OUT_PADDING + BUBBLE_WIDTH - ARROW_DELTA, OUT_PADDING + BUBBLE_HEIGHT);
        Point arrowBottom = new Point(OUT_PADDING + BUBBLE_WIDTH - ARROW_DELTA, OUT_PADDING + BUBBLE_HEIGHT + ARROW_HEIGHT);
        Polygon arrow = new Polygon(new int[] {arrowLeft.x, arrowRight.x, arrowBottom.x},
                                    new int[] {arrowLeft.y, arrowRight.y, arrowBottom.y}, 3);
        graphics.fillPolygon(arrow);
        graphics.setColor(Color.BLACK);
        graphics.drawLine(arrowLeft.x, arrowLeft.y, arrowBottom.x, arrowBottom.y);
        graphics.drawLine(arrowRight.x, arrowRight.y, arrowBottom.x, arrowBottom.y);

        graphics.setColor(Color.BLACK);
        int offset = OUT_PADDING + IN_PADDING;
        FontMetrics fm = graphics.getFontMetrics();
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(lines.get(i), offset, offset + (i + 1) * fm.getHeight());
        }
    }

    /**
     * Generate image for speech bubble!
     * 
     * @param g, the graphics object
     * @param s, the string to draw
     * @param f, the font we shall draw with
     * @param x, x-coordinate of the bubble relative to the arrow
     * @param y, y-coordinate of the bubble relative to the arrow
     * 
     * Note: JPEG format does not support transparency so we need to 
     * use TYPE_INT_RGB and not TYPE_INT_ARGB (i.e. no alpha).
     */
    public BufferedImage generateBubbleImage(String s) {
        // Need a temporary image to process text lines
        BufferedImage image = new BufferedImage(BUBBLE_WIDTH, BUBBLE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        FontMetrics fm = image.createGraphics().getFontMetrics();
        List<String> lines = this.formatText(s, fm);

        image = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        paintBubble(lines, graphics);
        graphics.dispose();

        return image;
    }
    
    public static Color randomGradient() {
    	return GRADIENTS[RAND.nextInt(GRADIENTS.length)];    	
    }
}
