package OMCE.OMCE.User;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    VENDEDOR("vendedor");

    private String role;

     UserRole (String role){
         this.role  = role ;
     }
     public String getRole(){
         return role;
     }
}
