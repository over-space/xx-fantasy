package com.fantasy.xxblog.auth;

import com.fantasy.xxutil.util.XXCipherUtils;
import com.fantasy.xxbase.vo.SimpleDataVO;
import com.fantasy.xxblog.entity.AccountEntity;
import com.fantasy.xxblog.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by lifang on 2015/1/24.
 */
public class AuthenticationRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(AuthenticationRealm.class);

    @Autowired
    private AccountService accountService;

    @Value("${system.login.locked.count}")
    private String lockedCount;

    @Value("${system.login.locked.seconds}")
    private String lockedSeconds;


    /**
     * 权限认证
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
//        SimpleDataVO principal = (SimpleDataVO) principals.getPrimaryPrincipal();

//        AccountEntity accountEntity = accountService.findById(Long.valueOf(principal.getId()));
//        simpleAuthenticationInfo.setRoles(adminEntity.getStrRoles());
//        simpleAuthenticationInfo.setStringPermissions(adminEntity.getStrPermission());

        WildcardPermissionEx wildcardPermissionEx = new WildcardPermissionEx();

        simpleAuthenticationInfo.addObjectPermission(wildcardPermissionEx);
        return simpleAuthenticationInfo;
    }

    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());
        String host = usernamePasswordToken.getHost();

        logger.info("login username : {}", username);
        logger.info("login password : {}", password);
        logger.info("login host : {}", host);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(String.valueOf(password))) {
            throw new UnknownAccountException();
        }
        AccountEntity accountEntity = accountService.findOneByName(username);
        if (accountEntity != null) {
            if (accountEntity.isLocked()) {
//                Date nowDate = new Date();
//                if (nowDate.getTime() - accountEntity.().getTime() > getMillisecond()) {
//                    accountEntity.setLocked(false);
//                } else {
                    throw new LockedAccountException();
//                }
            }
            if (XXCipherUtils.isMD5Equal(new String(accountEntity.getPassword()), password)) {
                //登录成功,登录次数加1,失败次数归零,并记录登录日志
                Long loginCount = accountEntity.getLoginSucceedCount() + 1L;
                accountEntity.setLoginSucceedCount(loginCount);
                accountEntity.setLoginFailedCount(0);
                accountService.merge(accountEntity);
                logger.info("登录成功");

                return new SimpleAuthenticationInfo(
                        new SimpleDataVO(accountEntity.getId(), accountEntity.getName()),
                        password,
                        getName());

            } else {
                //登录失败,登录失败次数加1
                logger.info("登录失败");
                long failedCount = accountEntity.getLoginFailedCount() + 1;
                if (failedCount >= getLockCount()) {
                    accountEntity.setLocked(true);
                }
                accountEntity.setLoginFailedCount(failedCount);
                accountService.merge(accountEntity);
                throw new IncorrectCredentialsException();
            }
        }
        throw new UnknownAccountException();
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    private long getMillisecond() {
        if (StringUtils.isBlank(lockedSeconds)) {
            return 30 * 1000 * 60;
        }
        Integer second = Integer.valueOf(lockedSeconds);
        return second * 1000 * 60;
    }

    private int getLockCount() {
        if (StringUtils.isBlank(lockedCount)) {
            return 5;
        }
        return Integer.valueOf(lockedCount);
    }

}