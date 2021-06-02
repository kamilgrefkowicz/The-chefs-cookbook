package pl.kamil.chefscookbook.user.domain;

public class MasterUserConfig {

    private MasterUserConfig(){}

    private static final UserEntity masterUser = new UserEntity("CCB", "");

    public static UserEntity getMasterUser() {
        masterUser.setId(1L);
        masterUser.setUuid("2a18bfca-b5e2-4bbf-9c54-38eb4eae9b1a");
        return masterUser;
    }
}
