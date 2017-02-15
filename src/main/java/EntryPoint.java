package main.java;

public class EntryPoint {
    public static void main(String[] args) {
        UserRepositoryImplements userRepositoryImplements=new UserRepositoryImplements();
        User user3=new User(8,"marina","dcs",2);
        //userRepositoryImplements.create(user3);
        //System.out.println(userRepositoryImplements.get("cdhjjhbs","dcs"));
        ///userRepositoryImplements.getAll();
        //userRepositoryImplements.update(user3);
        userRepositoryImplements.removeUser(8);
    }
}
