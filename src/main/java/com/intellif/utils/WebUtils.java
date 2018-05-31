package com.intellif.utils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.export.ExcelExportServer;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络操作工具类
 */
public abstract class WebUtils {

    private static Logger logger = Logger.getLogger(WebUtils.class);
    /**
     * 获取ServletRequestAttribute
     *
     * @return
     */
    private static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 保存到request域中
     *
     * @param key
     * @param value
     */
    public static void saveRequest(String key, Object value) {
        getServletRequestAttributes().setAttribute(key, value, ServletRequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 重定向的方法
     * @param url
     */
    public static void redirect(String url){
        try {
            getResponse().sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转发
     * @param url
     */
    public static void forward(String url){
        try {
            getRequest().getRequestDispatcher(url).forward(getRequest(),getResponse());
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveMapToRequest(Map<String,Object> map){
        if(map==null||map.size()==0)
            return;
        for(Map.Entry<String,Object> entry:map.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            saveRequest(key,value);
        }
    }

    /**
     * 保存session域中
     *
     * @param key
     * @param value
     */
    public static void saveSession(String key, Object value) {
        getServletRequestAttributes().setAttribute(key, value, ServletRequestAttributes.SCOPE_SESSION);
    }

    /**
     * 从request中获取对象
     *
     * @param key
     * @return
     */
    public static Object getFromRequest(String key) {
        return getServletRequestAttributes().getAttribute(key, ServletRequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 从session中获取对象
     *
     * @param key
     * @return
     */
    public static Object getFromSession(String key) {
        return getServletRequestAttributes().getAttribute(key, ServletRequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    /**
     * 获取session对象
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }


    public static String getPostData() {
        String str = (String) getFromRequest("str_bean");
        if (str == null) {
            str = getLine();
            saveRequest("str_bean", str);
        }
        return str;
    }

    /**
     * 从ServletRquest中获取字符串
     *
     * @return
     */
    private static String getLine() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(),"utf-8"));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>accepterData:"+sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                reader = null;
                e.printStackTrace();
            }
        }
        return null;
    }
    
    
    
    /**
     * 导出Excel表
     * @param clazz 用@Excel注解使用的类
     * @param datas 要导出的数据
     * @param fileName 文件名称
     * @param <T>
     */
    public static <T> void exportDatas(Class<T> clazz, List<T> datas, String fileName){
        try {
            WebUtils.getResponse().setHeader("content-Type", "application/vnd.ms-excel");
            WebUtils.getResponse().setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
            if (datas!=null) {
                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), clazz, datas);
                workbook.write(WebUtils.getResponse().getOutputStream());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    
    
    
     /**
     * 将接受到的excel流转变为对象
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> changeStreamToData(InputStream inputStream, Class<T> clazz){
        try {
            ImportParams params = new ImportParams();
            return ExcelImportUtil.importExcel(inputStream, clazz, params);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    
     /**
     * 导出多个sheet的excel
     * @param titles
     * @param entityCLass
     * @param data
     * @param fileName
     * @param <T>
     */
    public static <T> void exportMultiSheet(List<String> titles,List<Class<T>> entityCLass,List<List<T>> data,String fileName){
        try {
            WebUtils.getResponse().setHeader("content-Type", "application/vnd.ms-excel");
            WebUtils.getResponse().setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
            Workbook workbook = new HSSFWorkbook();
            if(titles.size()!=entityCLass.size()||titles.size()!=data.size()){
                throw new RuntimeException("前三个参数数量要相同必须一一对应");
            }
           int length = titles.size();
            for(int i=0;i<length;i++) {
                ExportParams params = new ExportParams();
                params.setTitle(titles.get(i));
                ExcelExportServer server = new ExcelExportServer();
                server.createSheet(workbook, params, entityCLass.get(i), data.get(i));
            }
            workbook.write(WebUtils.getResponse().getOutputStream());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前前几天日期
     * @param past
     * @return yyyy-MM-dd
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 根据输入日期获取其前几天日期
     * @param str yyyy-MM-dd
     * @param past
     * @return yyyy-MM-dd
     */
    public static String getPastDate(String str,int past){
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-past);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 计算日期间相隔天数
     * @param startTime yyyy-MM-dd 开始日期
     * @param endTime yyyy-MM-dd 结束日期
     * @return
     */
    public static Integer dateApart(String startTime,String endTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(endTime);
            Date date2 = format.parse(startTime);
            int a = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
            return a;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    
     /**
     * 获取几天后的日期
     * @param after
     * @return
     */
    public static String getAfterData(int after){
        Long time = new Date().getTime();
        time +=after*1000*60*60*24L;
        return formateTime(time,"yyyy-MM-dd");
    }


    public static String formateTime(Long time,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }


    /**
     * 获取制定日期后的几天
     * @param str
     * @param after
     * @return
     */
    public static String getAfterData(String str,int after){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            Long time = date.getTime();
            time +=after*1000*60*60*24L;
            return formateTime(time,"yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    
    
    public static boolean ping(String ipAddress){
        return ping(ipAddress,5,5000);
    }



    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        String pingCommand = null;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        if(osName.contains("Windows")){
            //将要执行的ping命令,此命令是windows格式的命令
            pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
            try {
                //执行命令并获取输出
                Process p = r.exec(pingCommand);
                if (p == null) {
                    return false;
                }
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                int connectedCount = 0;
                String line = null;
                while ((line = in.readLine()) != null) {
                    connectedCount += getCheckResult(line,osName);
                }
                //如果出现类似=23 ms ttl=64(TTL=64 Windows)这样的字样,出现的次数=测试次数则返回真
                return connectedCount == pingTimes;
                //return connectedCount >= 2 ? true : false;
            } catch (Exception ex) {
                ex.printStackTrace(); //出现异常则返回假
                return false;
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            //将要执行的ping命令,此命令是Linux格式的命令
            //-c:次数,-w:超时时间(单位/ms)  ping -c 10 -w 0.5 192.168.120.206
            //pingCommand = "ping " + " -c " + "4" + " -w " + "2 " + ipAddress;
            boolean ret = false;
            try {
                String [] command={"/bin/sh", "-c", "ping -c 4 " + ipAddress};
                Process process = Runtime.getRuntime().exec(command);
                ret = process.waitFor(timeOut, TimeUnit.SECONDS);
                if(!ret){
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>运行超时了。。。。。");
                } else {
                    ret = process.exitValue() == 0;
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        }


    }
    //若line含有=18 ms ttl=64字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line,String osName) {
        if(osName.contains("Windows")){
            Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                return 1;
            }
            return 0;
        }else{
            if(line.contains("ttl=")){
                return 1;
            }
        }
        return 0;
    }
    
    
    
     
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本机ip地址，并自动区分Windows还是linux操作系统
     *
     * @return String
     */
    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            //如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            }
            //如果是Linux操作系统
            else {
                boolean bFindIP = false;
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    //----------特定情况，可以考虑用ni.getName判断
                    //遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        ip = (InetAddress) ips.nextElement();
                        if (ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()   //127.开头的都是lookback地址
                                && ip.getHostAddress().indexOf(":") == -1) {
                            bFindIP = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

}