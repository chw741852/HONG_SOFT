package com.hong.core.security.controller;

import com.alibaba.fastjson.JSON;
import com.hong.core.generic.controller.impl.BaseControllerImpl;
import com.hong.core.security.domain.Role;
import com.hong.core.security.domain.User;
import com.hong.core.util.Tools;
import com.hong.manager.sys.dictionary.domain.SysCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by hong on 14-2-23 下午4:41.
 */
@Controller
@RequestMapping("/core/security/user")
public class UserController extends BaseControllerImpl {
    private final String BASEPATH = "/core/security/user";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/list")
    public String list(String message, final RedirectAttributes redirectAttributes) {
        if (message != null)    message = Tools.escapeCodeStr(message);
        redirectAttributes.addAttribute("queryModuleId", 131072);
        redirectAttributes.addFlashAttribute("flash_message", message);

        return "redirect:/manager/web/initPublicListInfo";
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView(BASEPATH + "/add");

        Map<String, String> sysCodeMap = new HashMap<String, String>();
        String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
        sysCodeMap.put("sexList", url + "'1'");
        sysCodeMap.put("typeList", url + "'30'");
        initSysCode(mv, sysCodeMap);

        String hql = "from " + Role.class.getName() + " a where a.enabled=1";
        List<Role> roles = genericService.executeObjectSql(hql);
        mv.addObject("roles", roles);

        return mv;
    }

    @RequestMapping("/save")
    public String save(User user, String roleIds, final RedirectAttributes redirectAttributes) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (roleIds != null && !roleIds.trim().equals("")) {
            Set<Role> roles = new HashSet<Role>();
            for (String s:roleIds.split(",")) {
                Role role = (Role)genericService.lookUp(Role.class, Long.parseLong(s));
                if (role != null) {
                    roles.add(role);
                }
            }
            user.setRoles(roles);
        }
        if (genericService.saveObject(user) != null) {
            redirectAttributes.addAttribute("message", "用户 " + user.getName() + " 新建成功");
        } else {
            redirectAttributes.addAttribute("message", "新建失败");
        }

        return "redirect:" + BASEPATH + "/list";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(long id) {
        ModelAndView mv = new ModelAndView(BASEPATH + "/edit");
        User user = (User)genericService.lookUp(User.class, id);
        if (user == null) {
            mv.addObject("message", "未找到ID为 " + id + " 的用户");
            mv.setViewName("redirect:" + BASEPATH + "/list");
        } else {
            Map<String, String> sysCodeMap = new HashMap<String, String>();
            String url = "from " + SysCode.class.getName() + " a where a.sysDictionary.key=";
            sysCodeMap.put("sexList", url + "1");
            sysCodeMap.put("typeList", url + "'30'");
            initSysCode(mv, sysCodeMap);

            String hql = "from " + Role.class.getName() + " a where a.enabled=1";
            List<Role> roles = genericService.executeObjectSql(hql);
            mv.addObject("roles", roles);

            mv.addObject("user", user);
        }

        return mv;
    }

    @RequestMapping("/update")
    public ModelAndView update(User user, String roleIds) {
        ModelAndView mv = new ModelAndView("redirect:" + BASEPATH + "/list");

        if (roleIds != null && !roleIds.trim().equals("")) {
            Set<Role> roles = new HashSet<Role>();
            for (String s:roleIds.split(",")) {
                Role role = (Role)genericService.lookUp(Role.class, Long.parseLong(s));
                if (role != null) {
                    roles.add(role);
                }
            }
            user.setRoles(roles);
        }

        User oldUser = (User)genericService.lookUp(User.class, user.getId());
        if (!oldUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        if (genericService.updateObject(user) != null) {
            mv.addObject("message", "用户 " + user.getUsername() + " 修改成功");
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
        User user = (User)genericService.lookUp(User.class, id);
        if (user == null) {
            mv.addObject("message", "未找到ID为" + id + "的用户");
            mv.setViewName("redirect:" + BASEPATH + "/list");
        } else {
            if (genericService.deleteObject(user) == false) {
                mv.addObject("message", "用户 " + user.getUsername() + " 删除失败");
                mv.setViewName("redirect:" + BASEPATH + "/list");
            } else {
                mv.addObject("message", "用户 " + user.getUsername() + " 删除成功");
            }
        }

        return mv;
    }

    @RequestMapping("/checkUsername")
    public void checkUsername(String username, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> map = new HashMap<String, String>();
        String sql = "select id from sys_user where username='" + username + "'";
        List<String[]> result = genericService.executeSql(sql);
        if (result != null && result.size() > 0) {
            map.put("errorMsg", "用户名重复");
        }

        String json = JSON.toJSONString(map);
        printJson(json, response);
    }
}
