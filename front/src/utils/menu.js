const menu = {
    list() {
        return [
            // ======================== 管理员菜单 ========================
            {
                "roleName": "管理员",
                "tableName": "users",
                "hasBackLogin": "是",
                "hasBackRegister": "否",
                "hasFrontLogin": "否",
                "hasFrontRegister": "否",
                "frontMenu": [],
                "backMenu": [
                    {
                        "menu": "【赛前】信息发布",
                        "child": [
                            {"buttons": ["新增","查看","修改","删除"], "menu": "竞赛信息管理", "menuJump": "列表", "tableName": "jingsaixinxi"},
                            {"buttons": ["查看","审核","删除"], "menu": "报名审核管理", "menuJump": "列表", "tableName": "jingsaibaoming"}
                        ]
                    },
                    {
                        "menu": "【赛中】作品评审",
                        "child": [
                            {"buttons": ["查看","删除"], "menu": "作品提交管理", "menuJump": "列表", "tableName": "zuopintijiao"},
                            {"buttons": ["新增","查看","修改","删除"], "menu": "作品评审打分", "menuJump": "列表", "tableName": "zuopindafen"}
                        ]
                    },
                    {
                        "menu": "【赛后】成绩证书",
                        "child": [
                            {"buttons": ["新增","查看","修改","删除","公示"], "menu": "获奖管理", "menuJump": "列表", "tableName": "huojiang"},
                            {"buttons": ["查看","删除"], "menu": "证书管理", "menuJump": "列表", "tableName": "zhengshu"},
                            {"buttons": ["查看"], "menu": "统计分析", "menuJump": "列表", "tableName": "tongji"}
                        ]
                    },
                    {
                        "menu": "系统管理",
                        "child": [
                            {"buttons": ["新增","查看","修改","删除"], "menu": "学生管理", "menuJump": "列表", "tableName": "xuesheng"},
                            {"buttons": ["新增","查看","修改","删除"], "menu": "教师管理", "menuJump": "列表", "tableName": "jiaoshi"},
                            {"buttons": ["新增","查看","修改","删除"], "menu": "班级类型", "menuJump": "列表", "tableName": "banjileixing"}
                        ]
                    }
                ]
            },

            // ======================== 学生菜单 ========================
            {
                "roleName": "学生",
                "tableName": "xuesheng",
                "hasBackLogin": "是",
                "hasBackRegister": "是",
                "hasFrontLogin": "否",
                "hasFrontRegister": "否",
                "frontMenu": [],
                "backMenu": [
                    {
                        "menu": "竞赛报名",
                        "child": [
                            {"buttons": ["查看","报名"], "menu": "竞赛信息", "menuJump": "列表", "tableName": "jingsaixinxi"},
                            {"buttons": ["查看","支付"], "menu": "我的报名", "menuJump": "列表", "tableName": "jingsaibaoming"}
                        ]
                    },
                    {
                        "menu": "作品管理",
                        "child": [
                            {"buttons": ["新增","查看","修改","删除"], "menu": "提交作品", "menuJump": "列表", "tableName": "zuopintijiao"},
                            {"buttons": ["查看"], "menu": "评审结果", "menuJump": "列表", "tableName": "zuopindafen"}
                        ]
                    },
                    {
                        "menu": "我的获奖",
                        "child": [
                            {"buttons": ["查看"], "menu": "获奖记录", "menuJump": "列表", "tableName": "huojiang"},
                            {"buttons": ["查看","打印"], "menu": "我的证书", "menuJump": "列表", "tableName": "zhengshu"},
                            {"buttons": ["查看"], "menu": "获奖公示", "menuJump": "列表", "tableName": "tongji"}
                        ]
                    }
                ]
            },

            // ======================== 教师菜单 ========================
            {
                "roleName": "教师",
                "tableName": "jiaoshi",
                "hasBackLogin": "是",
                "hasBackRegister": "是",
                "hasFrontLogin": "否",
                "hasFrontRegister": "否",
                "frontMenu": [],
                "backMenu": [
                    {
                        "menu": "竞赛管理",
                        "child": [
                            {"buttons": ["新增","查看","修改","删除"], "menu": "竞赛信息", "menuJump": "列表", "tableName": "jingsaixinxi"},
                            {"buttons": ["查看","审核","删除"], "menu": "报名审核", "menuJump": "列表", "tableName": "jingsaibaoming"}
                        ]
                    },
                    {
                        "menu": "评审管理",
                        "child": [
                            {"buttons": ["查看"], "menu": "作品查看", "menuJump": "列表", "tableName": "zuopintijiao"},
                            {"buttons": ["新增","查看","修改","删除"], "menu": "作品评分", "menuJump": "列表", "tableName": "zuopindafen"}
                        ]
                    },
                    {
                        "menu": "获奖管理",
                        "child": [
                            {"buttons": ["新增","查看","修改","删除","公示"], "menu": "获奖记录", "menuJump": "列表", "tableName": "huojiang"},
                            {"buttons": ["查看"], "menu": "证书管理", "menuJump": "列表", "tableName": "zhengshu"},
                            {"buttons": ["查看"], "menu": "统计报表", "menuJump": "列表", "tableName": "tongji"}
                        ]
                    }
                ]
            }
        ]
    }
}
export default menu;
