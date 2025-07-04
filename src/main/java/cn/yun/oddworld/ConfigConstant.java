package cn.yun.oddworld;


public class ConfigConstant {
	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

	public static final String SPRING_PROFILE_PRODUCTION = "prod";

	/**Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)*/
	public static final String SPRING_PROFILE_CLOUD = "cloud";

	public static String generateKeyWithHash(String fileName, String hash) {
		int extIndex = fileName.lastIndexOf('.');
		String baseName = (extIndex != -1) ? fileName.substring(0, extIndex) : fileName;
		String ext = (extIndex != -1) ? fileName.substring(extIndex) : "";
		return baseName + "-" + hash + ext;
	}
}
