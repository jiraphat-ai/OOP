package com.oop.projectmanagement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SortMemberTest {
	@Test
	public void setMember() {
		SortMember s = new SortMember() {

			@Override
			public List<Map<String, Object>> sortMember() {
				// TODO Auto-generated method stub
				throw new UnsupportedOperationException("Unimplemented method 'sortMember'");
			}
			// Implement any necessary methods or override behavior here
		};
		HttpSession session = null;
		List<Map<String, Object>> expected = new ArrayList<>();
		List<Map<String, Object>> actual = s.setMember(session);

		assertEquals(expected, actual);
	}
}
