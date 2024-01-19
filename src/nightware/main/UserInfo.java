package nightware.main;

public class UserInfo {
   private String name;
   private int uid;
   private String role;
   private String till;

   public String getName() {
      return this.name;
   }

   public int getUid() {
      return this.uid;
   }

   public String getRole() {
      return this.role;
   }

   public String getTill() {
      return this.till;
   }

   public UserInfo(String name, int uid, String role, String till) {
      this.name = name;
      this.uid = uid;
      this.role = role;
      this.till = till;
   }
}
