package com.hong.core.security.controller;

import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.core.security.domain.Authority;
import com.hong.core.security.domain.Role;
import com.hong.core.util.Tools;
import com.hong.manager.sys.dictionary.domain.SysCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * Created by hong on 14-2-25 下午1:49.
 */
@Controller
@RequestMapping("/core/security/role")
public class RoleController extends BaseControllerImpl {
    private final String BASEPATH = "/core/security/role";

    @RequestMapping("/list")
    public String list(String message, final RedirectAttributes redirectAttributes) {
        if (message != null)    message = Tools.escapeCodeStr(message);
        redirectAttributes.addAttribute("queryModuleId", 163840);
        redirectAttributes.addFlashAttribute("flash_message", message);

        return "redirect:/manager/web/initPublicListInfo";
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView(BASEPATH + "/add");

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("yesOrNo", url + "2");
        initSysCode(mv, sysCodeMap);

        String hql = "from " + Authority.class.getName() + " a where a.enabled=1";
        List<Authority> authorities = genericService.executeObjectSql(hql);
        mv.addObject("authorities", authorities);

        return mv;
    }

    @RequestMapping("/save")
    public ModelAndView save(Role role, String authorityIds) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        if (authorityIds != null && !authorityIds.trim().equals("")) {
            Set<Authority> authorities = new HashSet<Authority>();
            for (String authorityId:authorityIds.split(",")) {
                Authority authority = (Authority)genericService.lookUp(Authority.class, Long.parseLong(authorityId));
                if (authority != null) {
                    authorities.add(authority);
                }
            }
            role.setAuthorities(authorities);
        }
        if (genericService.saveObject(role) != null) {
            mv.addObject("message", "角色 " + role.getName() + " 新建成功");
        } else {
            mv.addObject("message", "新建失败");
        }

        return mv;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(long id) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/edit");

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("yesOrNo", url + "2");
        initSysCode(mv, sysCodeMap);

        Role role = (Role)genericService.lookUp(Role.class, id);
        if (role == null) {
            mv.setViewName("redirect:" + BASEPATH + "/list");
            mv.addObject("message", "未找到ID为 " + id + " 的角色");
        } else {
            String hql = "from " + Authority.class.getName() + " a where a.enabled=1";
            List<Authority> authorities = genericService.executeObjectSql(hql);
            mv.addObject("authorities", authorities);

            mv.addObject("role", role);
        }

        return mv;
    }

    @RequestMapping("/update")
    public ModelAndView update(Role role, String authorityIds) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        if (authorityIds != null && !authorityIds.trim().equals("")) {
            Set<Authority> authorities = new HashSet<Authority>();
            for (String authorityId:authorityIds.split(",")) {
                Authority authority = (Authority)genericService.lookUp(Authority.class, Long.parseLong(authorityId));
                if (authority != null) {
                    authorities.add(authority);
                }
            }
            role.setAuthorities(authorities);
        }
        if (genericService.updateObject(role) != null) {
            mv.addObject("message", "角色 " + role.getName() + " 修改成功");
        } else {
            mv.addObject("message", "修改失败");
        }

        return mv;
    }

    @RequestMapping("/lookup")
    public ModelAndView lookup(long id) {
        ModelAndView mv = edit(id);

        if (mv.getViewName().indexOf("redirect:") < 0)
            mv.setViewName(BASEPATH + "/lookup");

        return mv;
    }

    @RequestMapping("/delete")
    public ModelAndView delete(long id) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        Role role = (Role)genericService.lookUp(Role.class, id);
        if (role == null) {
            mv.addObject("message", "未找到ID为 " + id + " 的角色");
        } else {
            String sql = "select name from sys_user where id in (" +
                    " select user_id from sys_user_role where role_id=" + id + ")";
            List<String[]> result = genericService.executeSql(sql);
            if (result != null && result.size() > 0) {
                StringBuffer names = new StringBuffer();
                for (String[] str:result) {
                    names.append(str[0] + ", ");
                }
                mv.addObject("message", "角色 " + role.getName() + " 正在被 用户'" +
                        names.substring(0, names.lastIndexOf(",")) + "' 使用，删除失败");
            } else if (genericService.deleteObject(role) == true) {
                mv.addObject("message", "角色 " + role.getName() + " 删除成功");
            } else {
                mv.addObject("message", "角色 " + role.getName() + " 删除失败");
            }
        }

        return mv;
    }
}
