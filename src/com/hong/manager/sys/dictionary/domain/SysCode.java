package com.hong.manager.sys.dictionary.domain;

import com.hong.core.generic.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 14-1-9
 * Time: 上午9:46
 * To change this template use File | Settings | File Templates.
 * 字段表从表
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sys_code")
public class SysCode extends IdEntity {
    private String number;  // 代码
    private String name;    // 名称
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "sysDictionaryId")
    private SysDictionary sysDictionary = new SysDictionary();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SysDictionary getSysDictionary() {
        return sysDictionary;
    }

    public void setSysDictionary(SysDictionary sysDictionary) {
        this.sysDictionary = sysDictionary;
    }
}
