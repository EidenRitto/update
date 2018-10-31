
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @Auther: Eiden J.P Zhou
 * @Date: 2018/4/8
 * @Description:
 * @Modified By:
 */
public class MainWindow {
    public static void main(String[] args) {
        Properties properties = new Properties();
        String localVersion;
        String serverVision;
        String ServerURL;
        String jarName;
        char i = 'a';
        JarURLConnection jarConnection = null;
        //部署到项目中时需要修改为./update/config/config.properties
        String configFile = "./config/config.properties";
        String configVersionURL;
        String configVersionFile = "./update/version.properties";


        try {
            String path = new File(configFile).getAbsolutePath();
            System.out.println("配置文件路径:"+path);

            properties.load(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            System.out.println(properties);
            ServerURL = properties.getProperty("url").trim();
            jarName = ServerURL.split("/")[ServerURL.split("/").length-1];
            configVersionURL = properties.getProperty("configVersionURL").trim();
        } catch (IOException e) {
            System.out.println("读取更新配置文件失败");
            e.printStackTrace();
            return;
        }


        //带参数走后门
        if(args.length!=0){
            if(args[0].equals("new")){
                FileTool.downloadByNIO2(ServerURL,"./lib", jarName);
                System.out.println("更新成功...");
                return;
            }
        }

        System.out.println("正在检查更新……");
        try {
            //检查本地文件的版本信息
            System.out.println("本地文件目录:"+"./lib/"+jarName);
            JarFile localJarFile = new JarFile("./lib/"+jarName);
            localVersion = localJarFile.getManifest().getMainAttributes().getValue("Manifest-Version");
            localJarFile.close();
            System.out.println("当前版本："+localVersion);
        } catch (IOException e) {
            System.out.println("【警告】获取不到本地文件信息，正在创建新的本地文件……");
            localVersion = "";
        }

        try {
            System.out.println("版本文件下载链接:"+configVersionURL);
            FileTool.downloadByNIO2(configVersionURL,"./update", "version.properties");
            properties.load(
                    new InputStreamReader(new FileInputStream(new File(configVersionFile).getAbsolutePath()), "UTF-8"));
            serverVision = properties.getProperty(properties.getProperty("jarname").split("\\.")[0]).trim();
            System.out.println("最新版本："+serverVision);
        }catch (Exception e){
            System.out.println("加载更新失败……请检查网络后再试!");
            e.printStackTrace();
            return;
        }

//        try {
//            // 检查服务器上的文件信息，查看是否有更新
//            URL url = new URL("jar:"+ServerURL+"!/");
//            jarConnection = (JarURLConnection)url.openConnection();
//            jarConnection.setConnectTimeout(1);
//            jarConnection.setReadTimeout(1);
//            Manifest manifest = jarConnection.getManifest();
//            serverVision = manifest.getMainAttributes().getValue("Manifest-Version");
//            System.out.println("最新版本："+serverVision);
//        } catch (IOException e) {
//            System.out.println("加载更新失败……请检查网络后再试!");
//            System.out.println("url:"+e.getMessage());
//            return;
//        }

        if (!localVersion.equals("")){
            if(localVersion.equals(serverVision)){
                System.out.println("当前版本已是最新！");
                return;
            }else if (Double.parseDouble(localVersion)>Double.parseDouble(serverVision)){
                System.out.println("发现旧的版本，是否还原？(y/n)");
                while (true){
                    try {
                        i = (char)System.in.read();
                    } catch (IOException e) {
                        System.out.println("请输入正确格式！");
                    }
                    if(i=='y'){
                        break;
                    }else if(i=='n')
                    {return;}
                }
            }
        }
        System.out.println("下载中……请稍后……");
        System.out.println("下载链接:"+ServerURL);
        try {
            FileTool.downloadByNIO2(ServerURL,"./lib", jarName);
            System.out.println("更新成功...");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("下载更新文件失败");
        }
    }
}
