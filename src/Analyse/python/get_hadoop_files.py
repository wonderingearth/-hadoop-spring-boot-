import pymysql
db = pymysql.connect(host='127.0.0.1', user='root', passwd='wdb168728', db='work', port=3306, charset='utf8')
cursor = db.cursor()


def get_all():
    sql = 'select* from movie natural join type natural join comment'
    cursor.execute(sql)
    op = open('../../../../../../../../../../input/hadoop_datas.txt', 'a+', encoding='gbk')
    datas = cursor.fetchall()
    for data in datas:
        string = ''
        for i in data:
            string = string + str(i)
            string = string + '/'
        op.writelines(string)
        op.write('\n')


if __name__ == '__main__':
    get_all()
    print('hadoop输入文件生成成功！')
    cursor.close()
    db.close()
