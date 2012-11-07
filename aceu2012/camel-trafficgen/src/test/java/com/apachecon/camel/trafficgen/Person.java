package com.apachecon.camel.trafficgen;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.apachecon.camel.trafficgen.TrafficGeneratorTest.RandomCloneable;

public class Person implements Cloneable, RandomCloneable {
	private static final int BASE_ID = 1000000;
	private static final int BASE_PHONE = 5550000;
	// most popular names (source: http://names.mongabay.com/)
	private static final String[] FIRST_NAMES = {
		"JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD", "CHARLES", "JOSEPH", "THOMAS", 
		"CHRISTOPHER", "DANIEL", "PAUL", "MARK", "DONALD", "GEORGE", "KENNETH", "STEVEN", "EDWARD", "BRIAN", 
		"RONALD", "ANTHONY", "KEVIN", "JASON", "MATTHEW", "GARY", "TIMOTHY", "JOSE", "LARRY", "JEFFREY", 
		"FRANK", "SCOTT", "ERIC", "STEPHEN", "ANDREW", "RAYMOND", "GREGORY", "JOSHUA", "JERRY", "DENNIS", 
		"MARY", "PATRICIA", "LINDA", "BARBARA", "ELIZABETH", "JENNIFER", "MARIA", "SUSAN", "MARGARET", "DOROTHY", 
		"LISA", "NANCY", "KAREN", "BETTY", "HELEN", "SANDRA", "DONNA", "CAROL", "RUTH", "SHARON", 
		"MICHELLE", "LAURA", "SARAH", "KIMBERLY", "DEBORAH", "JESSICA", "SHIRLEY", "CYNTHIA", "ANGELA", "MELISSA", 
		"BRENDA", "AMY", "ANNA", "REBECCA", "VIRGINIA", "KATHLEEN", "PAMELA", "MARTHA", "DEBRA", "AMANDA"
		}; 
	private static final String[] LAST_NAMES = {
		"SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER", "WILSON", "MOORE", "TAYLOR", 
		"ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN", "THOMPSON", "GARCIA", "MARTINEZ", "ROBINSON"
	    };
	private static final String CSV_SEP = ", ";
	private static final int POS_PER_AREA = 40;
	private static Random random = new Random();
	private static AtomicInteger ID = new AtomicInteger(BASE_ID);
	private static AtomicInteger PHONE = new AtomicInteger(BASE_PHONE);
	private final String phone;
	private final String firstName;
	private final String lastName;
	private final String zip;
	private final String id;

	public Person(final String code) {
		// ignore range checks, safely assume safe usage
		int local = PHONE.incrementAndGet() % 10000000;
		phone = code + (local < 1000000 ? local + 1000000 : local);
		firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
		lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
		zip = code + (random.nextInt(POS_PER_AREA) + 10);
		id = newId();
	}

	public Person(final Person other) {
		this.phone = other.phone;
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.zip = other.zip;
		this.id = newId();
	}

	@Override
	public Person clone() {
		return new Person(this);
	}

	@Override
	public int cloneCount() {
		int r = random.nextInt(100);
		// 10% of consumers have 3 Rx, 20% have 2 Rx
		return r >= 90 ? 3 : r >= 70 ? 2 : 1;
	}
	
	public RandomCloneable newClone() {
		return clone();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(phone);
		buffer.append(CSV_SEP);
		buffer.append(firstName);
		buffer.append(CSV_SEP);
		buffer.append(lastName);
		buffer.append(CSV_SEP);
		buffer.append(id);
		buffer.append(CSV_SEP);
		buffer.append(zip);
		buffer.append(CSV_SEP);
		buffer.append("NA");
		return buffer.toString();
	}
	
	public static String newId() {
		return Integer.toString(ID.incrementAndGet());
	}

}

