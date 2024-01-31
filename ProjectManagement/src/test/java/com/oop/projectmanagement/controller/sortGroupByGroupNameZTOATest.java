package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.GroupFordetail;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class sortGroupByGroupNameZTOATest {
	@Test
	public void sortGroup() {
		sortGroupByGroupNameZTOA s = new sortGroupByGroupNameZTOA();
		List<GroupFordetail> expected = new ArrayList<>();
		List<GroupFordetail> actual = s.sortGroup();

		assertEquals(expected, actual);
	}
}
