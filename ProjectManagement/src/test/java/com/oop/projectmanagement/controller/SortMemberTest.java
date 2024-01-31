package com.oop.projectmanagement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SortMemberTest {

	@Test
	public void testSetMember() {
		SortMember s = Mockito.mock(SortMember.class);
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute(anyString())).thenReturn("someValue");
		List<Map<String, Object>> actual = s.setMember(session);
		// your assertions here
	}
	@Test
	public void setMemberTODO() {
		SortMember s = Mockito.mock(SortMember.class);
		HttpSession session = null;
		List<Map<String, Object>> expected = new ArrayList<>();
		List<Map<String, Object>> actual = s.setMember(session);

		assertEquals(expected, actual);
	}
}
