package org.charitable.app.domain.entity.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.base.Base;

@Getter
@Setter
@AllArgsConstructor
public class Organization extends Base {
    private String name;
    private String address;
    private Float latitude;
    private Float longitude;
    private String govtId;
    private String contactNumber;
    private Auth auth;
}
