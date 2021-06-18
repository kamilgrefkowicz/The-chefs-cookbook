package pl.kamil.chefscookbook.user.domain;

public class MasterUserConfig {

    private MasterUserConfig(){}

    public static UserEntity getMasterUser() {
        UserEntity masterUser = new UserEntity("CCB", "$2y$12$OBdkCJNVbo5oRo6EdSWKme7W0cSg0TP3ZZkR9Z9FolzrgRMg.MESm" );
        masterUser.setId(1L);
        masterUser.setUuid("2a18bfca-b5e2-4bbf-9c54-38eb4eae9b1a");
        return masterUser;
    }
}
