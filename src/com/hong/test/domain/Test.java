package com.hong.test.domain;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-11-8
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "hong_test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    @Version
    @Column(name = "version")
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
