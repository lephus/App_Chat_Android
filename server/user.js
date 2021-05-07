var mysql = require('mysql');

// connect den mysql
var conn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'appteams'
});

function puser(id, username, password, fullname, gender, address, level){
    this.id       = id;
    this.username = username;
    this.password = password;
    this.fullname = fullname;
    this.gender   = gender;
    this.address  = address;
    this.level    = level;
}
var Users = [];

puser.prototype.ouput = function () {
    console.log("USER:id = " + this.id + "userName:=  " + this.username + " Password:= " + this.password + " fullName:= " + this.fullname + " gender:= " + this.gender + " addRess:= " + this.address + " level:= " + this.level);
}
const ok = function () {
    //ket noi den database.
    conn.connect(function (err) {
        //neu co loi thi in ra
        if (err) throw err.stack;
        //neu thanh cong
        //Bat đau transaction
        conn.beginTransaction(function (err) {
            if (err) throw err;
            //Thuc thi cau lenh thu 1
            conn.query("SELECT * FROM user", function (err, results) {
                //neu co loi
                if (err) {
                    //rollback qua trinh
                    return conn.rollback(function () {
                        throw err;
                    });
                }
                //Neu khong loi, lay ra id vua insert
                for (i = 0; i < results.length; i++) {
                    var res = results[i];
                    var tmp = new puser(res["id"], res["username"], res["password"], res["fullname"], res["gender"], res["address"], res["level"]);
                    Users.push(tmp);
                }
                //console.log(Users);
                 haha = Users.slice();
                //Nếu các câu truy vấn đều thành công thì thực hiện commit
                conn.commit(function (err) {
                    //nếu commit lỗi
                    if (err) {
                        //rollback transaction
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    //Nếu thành công thì ...
                    //  console.log('truy van sql thanh cong');
                });

            });
        })
    });
    return Users;
}
module.exports = ok;



