namespace java com.hef.demo.thrift.api

struct User {
  1: i32 id,
  2: string name
}

service UserService {
   User findUser(1 : i32 id)
}

struct Order {
  1: i32 id,
  2: string name,
  3: double amount
}

service OrderService {
   Order findOrder(1 : i32 id)
}