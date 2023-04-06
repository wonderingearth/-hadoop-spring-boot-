import pymysql
db = pymysql.connect(host='127.0.0.1', user='root', passwd='wdb168728', db='work', port=3306, charset='utf8')
cursor = db.cursor()


def read_file():
    sql = 'insert into work.movie_all values (%s, %s, %s, %s, %s)'
    with open('../../../../../../../../output/part-r-00000', 'r', encoding='utf-8') as op:
        for line in op.readlines():
            line = line.strip()
            strings = line.split(',', 4)
            movie_id = int((strings[0].split('='))[1])
            movie_name = (strings[1].split('='))[1].strip('\'').strip()
            movie_type = (strings[2].split('='))[1].strip('\'')
            movie_rate = float((strings[3].split('='))[1])
            movie_hotkeys = (strings[4].split('='))[1].strip('\'')
            cursor.execute(sql, (movie_id, movie_name, movie_type, movie_rate, movie_hotkeys))
            db.commit()
    with open('../../../../../../../../output/part-r-00001', 'r', encoding='utf-8') as op:
        for line in op.readlines():
            line = line.strip()
            strings = line.split(',', 4)
            movie_id = int((strings[0].split('='))[1])
            movie_name = (strings[1].split('='))[1].strip('\'').strip()
            movie_type = (strings[2].split('='))[1].strip('\'')
            movie_rate = float((strings[3].split('='))[1])
            movie_hotkeys = (strings[4].split('='))[1].strip('\'')
            cursor.execute(sql, (movie_id, movie_name, movie_type, movie_rate, movie_hotkeys))
            db.commit()


if __name__ == '__main__':
    read_file()
    print('将hadoop文件写入到movie_all表成功！')
    cursor.close()
    db.close()
