package cn.ibrother.tools.data.init;

/**
 * 命令行解释处理
 * @author oyxz
 * @since 2011-10-13
 * @version 1.0
 */
import com.beust.jcommander.Parameter;

public class CmdParameter {
	@Parameter(names = {"-act"}, description = "执行动作")
	public Integer act = 1;	// 类型:1,export; 2,import

	@Parameter(names = "-db", description = "数据库名称")
	public String db = "";
	
	@Parameter(names = "-path", description = "excel路径")
	public String path = "";

	@Parameter(names = "-file", description = "特定excel文件列表,空格分隔多个")
	public String file = "";
	
	@Parameter(names = "-gameName", description = "游戏名称")
	public String gameName = "";
}