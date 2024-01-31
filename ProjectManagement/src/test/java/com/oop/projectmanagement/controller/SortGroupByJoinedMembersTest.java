package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.GroupFordetail;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SortGroupByJoinedMembersTest {
	@Test
	public void sortGroup() {
		SortGroupByJoinedMembers s = new SortGroupByJoinedMembers();
		List<GroupFordetail> expected = new ArrayList<>();
		List<GroupFordetail> actual = s.sortGroup();

		assertEquals(expected, actual);
	}
}
