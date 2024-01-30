package com.oop.projectmanagement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class sortMemberZtoATest {
	@Test
	public void sortMember() {
		sortMemberZtoA s = new sortMemberZtoA();
		List<Map<String, Object>> expected = new ArrayList<>();
		List<Map<String, Object>> actual = s.sortMember();

		assertEquals(expected, actual);
	}
}
