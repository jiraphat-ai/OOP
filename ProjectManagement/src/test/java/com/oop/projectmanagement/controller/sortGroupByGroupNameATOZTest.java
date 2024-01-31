package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.GroupFordetail;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class sortGroupByGroupNameATOZTest {
	@Test
	public void sortGroup() {
		sortGroupByGroupNameATOZ s = new sortGroupByGroupNameATOZ();
		List<GroupFordetail> expected = new ArrayList<>();
		List<GroupFordetail> actual = s.sortGroup();

		assertEquals(expected, actual);
	}
}
