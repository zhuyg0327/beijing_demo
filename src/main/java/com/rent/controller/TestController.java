package com.rent.controller;

import com.alibaba.fastjson.JSONObject;
import com.rent.entity.BaseAppUser;
import com.rent.entity.People;
import com.rent.entity.User;
import com.rent.service.BaseAppUserService;
import com.rent.service.UserService;
import com.rent.util.DocUtil;
import com.rent.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    BaseAppUserService baseAppUserService;
    @Autowired
    UserService userService;
//    public static void main(String[] args) {
//        TestController t = new TestController();
//        t.test1();
//    }

    /**
     * Java实现JSONObject对象与Json字符串互相转换
     */
    public void test() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "wjw");
        jsonObject.put("age", 22);
        jsonObject.put("sex", "男");
        jsonObject.put("school", "商职");
        String jsonStr = JSONObject.toJSONString(jsonObject);
        System.out.println(jsonStr);
        jsonObject.clear();
        String jsonStr1 = "{\"school\":\"商职\",\"sex\":\"男\",\"name\":\"wjw\",\"age\":22}";
        jsonObject = JSONObject.parseObject(jsonStr1);
        System.out.println(jsonObject.getString("name"));
        System.out.println(jsonObject.getInteger("age"));
    }

    /**
     * 实体类 转 JSONObject对象
     */
    public void test1() {
        People people = new People();
        people.setId("00001");
        people.setUserName("李白");
        people.setSort("1");
        // 转换为json字符串
        String personStr = JSONObject.toJSONString(people);
        System.out.println("personStr:" + personStr);
        // 转换为json对象
        JSONObject personObject = JSONObject.parseObject(personStr);
        System.out.println("personObject:" + personObject);
        System.out.println("name:" + personObject.getString("userName"));
    }

    @RequestMapping("/test2")
    public void test2() {
        List<User> list1 = userService.queryAll();
        List<BaseAppUser> list2 = baseAppUserService.queryAll();
        List<Object> list3 = new ArrayList<>();
        // 根据单个字段字段去除重复
        User user1 = new User();
        user1.setId("001");
        user1.setAccount("lb");
        user1.setUserPassword("123456");
        user1.setUserName("李白");
        list1.add(user1);
        List<User> userList = list1.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User -> User.getId()))), ArrayList::new));
        //获取list2的id信息
        List<String> ids = list2.stream().map(BaseAppUser::getId).collect(Collectors.toList());
        for (User user : list1) {
            //如果ids集合不包含list1中的id，那就放入list3里面
            if (!ids.contains(user.getId())) {
                list3.add(user);
            }
        }
        Response.json(list1);
    }

    @RequestMapping("/test3")
    public void test3() {
        List<User> list1 = userService.queryAll();
        List<BaseAppUser> list2 = baseAppUserService.queryAll();
        List<User> list = new ArrayList<>();
        for (BaseAppUser baseuser : list2) {
            User user = new User();
            user.setId(baseuser.getId());
            user.setAccount(baseuser.getAccount());
            user.setUserId(baseuser.getUserId());
            user.setUserName(baseuser.getUserName());
            user.setUserPassword(baseuser.getUserPassword());
            user.setTelephone(baseuser.getTelephone());
            user.setSex(baseuser.getSex());
            user.setSort(baseuser.getSort());
            user.setStatus(baseuser.getStatus());
            user.setDeptId(baseuser.getDeptId());
            user.setDeptName(baseuser.getDeptName());
            user.setPower(baseuser.getPower());
            list.add(user);
        }
        if (list.size() > 0 && list != null) {
            userService.updateBatch(list);
        }
        Response.json(list);
    }

    /**
     * 读取Excel文件
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/readExcel")
    protected void readExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            //String encoding = "GBK";
            File excel = new File("F:\\项目练习\\readExcelMaven\\test.xls");
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                    Response.json("result", "文件类型错误!");
                    return;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                System.out.println(cell.toString());
                                Response.json(cell.toString());
                            }
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
                Response.json("result", "找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Word文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    @RequestMapping("/readWord")
    public void ReadDoc() throws IOException {
        String resullt = "";
        File file = new File("F:\\项目练习\\readWord\\朱阳刚.docx");
        if (file.isFile() && file.exists()) {
            String names[] = file.getName().split("\\.");
            String endname = "";
            if (names != null && names.length > 0) {
                endname = names[1];
            }
            //首先判断文件中的是doc/docx
            try {
                if ("doc".equals(endname)) {
                    InputStream is = new FileInputStream(file);
                    WordExtractor wr = new WordExtractor(is);
                    resullt = wr.getText();
                    wr.close();
                } else if ("docx".equals(endname)) {
                    OPCPackage opcPackage = POIXMLDocument.openPackage(file.getPath());
                    POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                    resullt = extractor.getText();
                    extractor.close();
                } else {
                    Response.json("resullt", "此文件不是word文件");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Response.json("result", "找不到指定的文件");
            return;
        }
        System.out.println(resullt);
        Response.json(resullt);
    }

    @RequestMapping("/test4")
    public void test4() {
        File file = new File("F:\\项目练习\\readWord\\test.doc");
        String path = file.getPath();
        Map<String, Object> map = new HashMap<>();
        map = DocUtil.readWordImage(file);
        Response.json(map);
    }

    /**
     * 校验时间段是否有重合
     */
    /**
     * 判断给定时间段[startX,endX]是否与已知时间段存在交集,不存在返回true
     *
     * @param startX     例：2020/02/18 00:00:00
     * @param endX       例：2020/04/18 24:00:00
     * @param originList 正序存储的时间段集合[{start:A0,end:A1},{start:B0,end:B1}]
     * @return
     */
    public static boolean isNormal(String startX, String endX, List<Map<String, Object>> originList) {
        if (StringUtils.isNotBlank(startX) && StringUtils.isNotBlank(endX)) {
            startX = formatDate(startX);
            endX = formatDate(endX);
            if (originList != null && originList.size() > 0) {
                List<String> startList = new ArrayList<String>();
                List<String> endList = new ArrayList<String>();
                for (int i = 0; i < originList.size(); i++) {
                    startList.add(formatDate((String) originList.get(i).get("start")));
                    endList.add(formatDate((String) originList.get(i).get("end")));
                }
                String minStart = startList.get(0);
                String maxEnd = endList.get(endList.size() - 1);
                if (endX.compareTo(minStart) < 0) {
                    return true;
                }
                if (startX.compareTo(maxEnd) > 0) {
                    return true;
                }
                int minStartIndex = 0;
                if (startX.compareTo(minStart) > 0) {
                    if (startX.compareTo(endList.get(minStartIndex)) > 0) {// 即startX>A1
                        for (int i = minStartIndex; i < startList.size(); i++) {
                            if (startX.compareTo(startList.get(i)) < 0) {
                                if (endX.compareTo(startList.get(i)) < 0) {
                                    return true;
                                }
                            } else {
                                minStartIndex += 1;
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static String formatDate(String date) {
        if (date != null) {
            date = date.replace("/", "").replace(":", "").replace(" ", "");
        } else {
            date = "";
        }
        return date;
    }

    @RequestMapping("/test5")
    public void checkData(String start,String end) {
        //默认重合为false，不重合为ture
        Boolean flag = false;
        List<Map<String, Object>> originList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("start", "2020:11:21 12:15:00");
        map1.put("end", "2020/11/21 23:11:00");
        originList.add(map1);
        flag = isNormal(start, end, originList);
        if(flag){
            Response.json("success","时间段不重合");
        }else {
            Response.json("success","时间段重合");
        }
    }

}
