package util;

import java.util.ArrayList;
import java.util.List;

public class PubUtil {
	public List<Integer> integers2List(Integer[] objs){
		List<Integer> list = new ArrayList<Integer>();
		for (Integer integer : objs) {
			list.add(integer);
		}
		return list;
	}
}
