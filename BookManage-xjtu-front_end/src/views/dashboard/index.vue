<template>
  <div class="dashboard-container">
    <div title="欢迎页面" style="padding: 50px; overflow: hidden; color: #409eff;font: lighter 16px 'Lucida Sans Unicode';"
         data-options="iconCls:'icon-heart',plain:true">
        <b style="font-size: 36px; line-height: 30px; height: 30px;">欢迎使用图书管理系统</b>
        <p>西安交通大学软件学院 数据库大作业</p>
        <p>小组成员：陈达 王奕书 徐崇凌 滕霄鹏 李东昊（排名不分先后）</p>
        <p>指导老师：刘帅</p>
        <p>开发周期: 2023/10/29 —— 2023/12/3</p>
        <hr/>
        <b>项目开发环境介绍</b>
        <p>操作系统: Windows 10 home</p>
        <p>开发工具: Intellij IDEA 2021.2.1(Ultimate Edition)</p>
        <p>Java版本: 1.8.0</p>
        <p>服务器: Tomcat 8.5</p>
        <p>数据库: MySQL 5.7</p>
        <p>前端技术: HTML+CSS+JavaScript+Vue+Axios+ElementUI</p>
        <p>后端技术: Java+SpringBoot+MyBatis+MySQL</p>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'id',
      'name',
      'roles'
    ])
  }
}
</script>
