import pymysql
import json

db = pymysql.connect(host='127.0.0.1', user='root', passwd='wdb168728', db='work', port=3306, charset='utf8')
cursor = db.cursor()


def get_movie_ranks(movie_id):
    sql = 'select comment_star from work.comment where movie_id = %s and comment_star<6'
    cursor.execute(sql, movie_id)
    ranks = cursor.fetchall()
    total = 0
    sum = 0
    for rank in ranks:
        total += rank[0]
        sum += 1
    return total / sum


def get_type_ranks():
    sql = 'select movie_type from work.type group by movie_type'
    sql1 = 'select movie_id from work.type where movie_type = %s'
    sql2 = 'select comment_star from work.comment where movie_id = %s and comment_star<6'
    cursor.execute(sql)
    types = cursor.fetchall()
    List = []
    for type in types:
        dic = {}
        cursor.execute(sql1, type)
        movie_ids = cursor.fetchall()
        movie_stars = 0
        num = 0
        for movie_id in movie_ids:
            cursor.execute(sql2, movie_id)
            stars = cursor.fetchall()
            movie_star = 0
            Sum = 0
            for star in stars:
                Sum += 1
                movie_star += star[0]
            movie_stars += movie_star / Sum
            num += 1
        dic['name'] = type[0]
        dic['value'] = '%.2f' % (movie_stars / num)
        List.append(dic)
    json_str = json.dumps(List, ensure_ascii=False)
    with open('../../../../../../resources/static/json/type_ranks.json', 'a+', encoding='utf-8') as json_writer:
        json_writer.write(json_str)


if __name__ == '__main__':
    get_type_ranks()
    print('输出各个类型的评分成功！')
    cursor.close()
    db.close()
