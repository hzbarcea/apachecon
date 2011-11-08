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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeechBubble {
    private static final Logger LOG = LoggerFactory.getLogger(SpeechBubble.class);

    private static final int MAX_WIDTH = 320;
    private static final int MAX_HEIGHT = 240;
    private static final int PIC_WIDTH = 48;
    private static final int OUT_PADDING = 8;
    private static final int IN_PADDING = 4;
    private static final int ARROW_HEIGHT = 12;
    private static final int ARROW_WIDTH = 8;
    private static final int ARROW_DELTA = 24;
    private static final Font DEF_FONT = new Font(Font.SERIF, Font.PLAIN, 12);

    private int width;
    private int height;
    private Font font;

    public SpeechBubble() {
        this(MAX_WIDTH, MAX_HEIGHT, DEF_FONT);
    }

    public SpeechBubble(int width, int height, Font font) {
        boolean outofbounds = width > MAX_WIDTH || width < 2 * OUT_PADDING + 2 * ARROW_DELTA + ARROW_WIDTH;
        this.setWidth(outofbounds ? MAX_WIDTH : width);
        outofbounds = height > MAX_HEIGHT || height < 2 * OUT_PADDING + 2 * PIC_WIDTH + IN_PADDING;
        this.setHeight(outofbounds ? MAX_HEIGHT : height);
        this.setFont(font != null ? font : DEF_FONT);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
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

        int bubbleWidth = getWidth() - 2 * OUT_PADDING;
        int bubbleHeight = getHeight() - 2 * OUT_PADDING - PIC_WIDTH - IN_PADDING;
        int lineCount = bubbleHeight / fm.getHeight();

        List<String> lines = new ArrayList<String>(lineCount);
        String[] tokens = text.split(" ");
        String line = "";
        int lineWidth = 0;

        // Use a dummy Image to calculate FontMetrics
        for (String word : tokens) {
            int w = fm.charsWidth((word + " ").toCharArray(), 0, word.length() + 1);
            lineWidth += w;
            if (lineWidth > bubbleWidth) {
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
        FontMetrics fm = graphics.getFontMetrics();
        int bubbleWidth = width - 2 * OUT_PADDING;
        int bubbleHeight = lines.size() * fm.getHeight() + 2 * IN_PADDING;

        graphics.setPaint(new GradientPaint(OUT_PADDING, OUT_PADDING, Color.CYAN, 
            OUT_PADDING, bubbleHeight - OUT_PADDING, Color.WHITE));

        // First thing is to draw the bubble with a thin black outline
        int arc = IN_PADDING * 4;
        graphics.fillRoundRect(OUT_PADDING, OUT_PADDING, bubbleWidth, bubbleHeight, arc, arc);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(OUT_PADDING, OUT_PADDING, bubbleWidth, bubbleHeight, arc, arc);

        // With an arror at the bottom
        // TODO: add orientation, maybe
        graphics.setColor(Color.WHITE);
        Point arrowLeft = new Point(OUT_PADDING + bubbleWidth - ARROW_DELTA - ARROW_WIDTH, OUT_PADDING
                                                                                           + bubbleHeight);
        Point arrowRight = new Point(OUT_PADDING + bubbleWidth - ARROW_DELTA, OUT_PADDING + bubbleHeight);
        Point arrowBottom = new Point(OUT_PADDING + bubbleWidth - ARROW_DELTA, OUT_PADDING + bubbleHeight
                                                                               + ARROW_HEIGHT);
        Polygon arrow = new Polygon(new int[] {arrowLeft.x, arrowRight.x, arrowBottom.x},
                                    new int[] {arrowLeft.y, arrowRight.y, arrowBottom.y}, 3);
        graphics.fillPolygon(arrow);
        graphics.setColor(Color.BLACK);
        graphics.drawLine(arrowLeft.x, arrowLeft.y, arrowBottom.x, arrowBottom.y);
        graphics.drawLine(arrowRight.x, arrowRight.y, arrowBottom.x, arrowBottom.y);

        graphics.setColor(Color.BLACK);
        int offset = OUT_PADDING + IN_PADDING;
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
     */
    public BufferedImage generateBubbleImage(String s) {
        // Need a temporary image to process text lines
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        FontMetrics fm = image.createGraphics().getFontMetrics();
        List<String> lines = this.formatText(s, fm);

        // Now we can create the final image with the correct dimensions
        int h = lines.size() * fm.getHeight() + PIC_WIDTH + 2 * OUT_PADDING + IN_PADDING;
        image = new BufferedImage(width, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, h);
        paintBubble(lines, graphics);

        return image;
    }
}
