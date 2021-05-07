// goi thu vien can thiet vao code
var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io")().listen(server);
var fs = require("fs");
var mysql = require('mysql');
console.log("Server Running...");

//connect mysql
var conn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'appteams'
});

// server lang nghe even tu client
server.listen(process.env.PORT || 3000);
    app.get("/", function (req, res) {
         res.sendFile(__dirname + "/index.html");
    })
io.sockets.on('connection', function (socket) {
    console.log("Connect Successfully...");

    socket.on('Client-send-data', function (data) {
        console.log("Server nhan: " + data);
        // gui data ve tat ca thiet bi client
        io.sockets.emit('server-send-all', {content : data });
        // gui data ve 1 thiet bi cu the
    });

    // cac  yeu cau ve login
    socket.on('client-login', function (datalogin) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        console.log(datalogin);
        var c = datalogin.split("-"); 
        var C_username = c[0];
        var C_pass = c[1];
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
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    for (i = 0; i < results.length; i++) {
                        var res = results[i];
                        var ok = "-1";
                        if (res["username"] == C_username && res["password"] == C_pass) {
                            ok = res["id"];
                            ok += "-" + res["level"];
                            socket.emit("server-send-login", { checklogin: ok });
                        } else {
                                    ok = "-1";
                            socket.emit("server-send-login", { checklogin: ok});
                        }
                    }
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
    });
    // cac yeu cau ve view profile
    socket.on('Client-send-viewProfile', function (dataprofile) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var c = dataprofile;
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user WHERE user.id = "+c, function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    socket.emit("server-send-viewProfile", { profile: results });
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
    });
    // view room cho tung client
    socket.on('Client-send-viewRooom', function (ID) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var arrayRom = [];
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM room ", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    for(var j = 0; j< results.length; j++){
                        var idlistuser = results[j];
                        var tmp = idlistuser["idlistuser"].split("-");
                        for(var i=0; i<tmp.length; i++){
                            if(tmp[i] == ID){
                                arrayRom.push(idlistuser["id"]+"<"+idlistuser["idlistuser"]+">");
                            }
                        }
                    }
                    socket.emit("server-send-viewRoom", { room: arrayRom });
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
    });
    // view thanh vien cho tat ca client
    socket.on('Client-send-thanhvien', function (ID) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user ", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("server-send-thanhvien", { thanhvien: results });
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
    });
    //add rom
    socket.on('Client-send-addromm', function (data1) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var data = data1.split("#"); // 1-2-3-#1
        var arrayRom = [];
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("INSERT INTO `room` (`id`, `idadmin`, `idlistuser`) VALUES (NULL, '"+data[1]+"', '"+data[0]+"');", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
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
    });
       //view join
    socket.on('Client-send-join', function (LISTUSER) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var data = LISTUSER.split("-"); // 1-2-3-#1
        var array = [];
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
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    for(var i=0; i<results.length; i++){
                        var user = results[i];
                        for(var j=0; j<data.length; j++){
                            if(user["id"]==data[j]){
                                array.push(user["fullname"]+"->"+user["id"]);
                                break;
                            }
                        }
                    }
                    socket.emit("server-send-join", {data : array});
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
    });
    //view char1
    socket.on('Client-send-chat1', function (IDROOM) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM mess WHERE idroom = "+IDROOM, function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    console.log(IDROOM);
                    console.log(results);
                    socket.emit("server-send-mess1", {datamess1 : results});
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
    });
    //view char2
    socket.on('Client-send-chat2', function (DATA) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var res = DATA.split("-");
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("INSERT INTO `mess` (`iduser`, `content`, `idroom`, `date`) VALUES ('"+res[0]+"', '"+res[1]+"', '"+res[2]+"', '');", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("server-send-mess2", { data: DATA });
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
    });
    //add user
    socket.on('Client-send-ADDUSER', function (data) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var d = data.split("#"); // username#pass#fullname#gender#address#level
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("INSERT INTO `user` (`id`, `username`, `password`, `fullname`, `gender`, `address`, `level`) VALUES (NULL, '"+d[0]+"', '"+d[1]+"', '"+d[2]+"', '"+d[3]+"', '"+d[4]+"', '"+d[5]+"');", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
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
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user", function (err, results2) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("adduser", { data: results2 });
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
    });
    //LENAD
    socket.on('LENAD', function (ID) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("UPDATE `user` SET `level` = '1' WHERE `user`.`id` = "+ID+";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
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
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user", function (err, results2) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("adduser", { data: results2 });
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
    });
    //XOAAD
    socket.on('XOAAD', function (ID) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("UPDATE `user` SET `level` = '0' WHERE `user`.`id` = " + ID + ";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
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
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user", function (err, results2) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("adduser", { data: results2 });
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
    });
    //DELETEUSER
    socket.on('DELETEUSER', function (ID) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("DELETE FROM `user` WHERE `user`.`id` = "+ID+";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
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
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user", function (err, results2) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("adduser", { data: results2 });
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
    });
    //view newfeed
    socket.on('viewnewfeed', function (DATA) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM newfeed WHERE idroom = "+DATA+";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    socket.emit("server-viewnewfeed", { data: results });
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
    });
    //add newfeed
    socket.on('addnewfeed', function (DATA) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var res = DATA.split("#");
        var auth = ":)) ";
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user WHERE id = "+res[0], function (err, resul) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    var k = resul[0];
                    auth = k["fullname"];
                    //Bat đau transaction
                    conn.beginTransaction(function (err) {
                        if (err) throw err;
                        //Thuc thi cau lenh thu 1
                        conn.query("INSERT INTO `newfeed` (`idroom`, `title`, `content`, `auth`) VALUES ('" + res[3] + "', '" + res[1] + "', '" + res[2] + "', '" + auth + "');", function (err, results123) {
                            //neu co loi
                            if (err) {
                                return conn.rollback(function () {
                                    throw err;
                                });
                            }
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
         
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM newfeed WHERE idroom = "+res[3]+";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("server-view1", { data: results });
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
    });
    //add newfeed ALL
    socket.on('addnewfeedALL', function (DATA) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        var res = DATA.split("#");
        var auth = ":)) ";
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM user WHERE id = " + res[0], function (err, resul) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    var k = resul[0];
                    auth = k["fullname"];
                    //Bat đau transaction
                    conn.beginTransaction(function (err) {
                        if (err) throw err;
                        //Thuc thi cau lenh thu 1
                        conn.query("INSERT INTO `newfeed` (`idroom`, `title`, `content`, `auth`) VALUES ('-1', '" + res[1] + "', '" + res[2] + "', '" + auth + "');", function (err, results123) {
                            //neu co loi
                            if (err) {
                                return conn.rollback(function () {
                                    throw err;
                                });
                            }
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

            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM newfeed WHERE idroom = " + -1 + ";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("all", { data: results });
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
    });
    //client
    socket.on('Client', function (DATA) {
        var conn = mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: '',
            database: 'appteams'
        });
        conn.connect(function (err) {
            //neu co loi thi in ra
            if (err) throw err.stack;
            //neu thanh cong
            //Bat đau transaction
            conn.beginTransaction(function (err) {
                if (err) throw err;
                //Thuc thi cau lenh thu 1
                conn.query("SELECT * FROM newfeed WHERE idroom = " + -1 + ";", function (err, results) {
                    //neu co loi
                    if (err) {
                        return conn.rollback(function () {
                            throw err;
                        });
                    }
                    io.sockets.emit("all", { data: results });
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
    });
});





