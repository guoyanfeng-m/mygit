package util;

public class TTerminal {
	private String name;
	private String diskUsage;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(String diskUsage) {
		this.diskUsage = diskUsage;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "终端名称:"+name+"  终端使用率为"+diskUsage+"%";
	}
}
