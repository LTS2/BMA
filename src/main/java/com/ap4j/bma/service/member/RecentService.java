package com.ap4j.bma.service.member;

public interface RecentService {

	public void recentCheck(int id, String nickname);
	public int recentDelete(Integer id, String nickname);
}
