import pymysql
db = pymysql.connect(host='127.0.0.1', user='root', passwd='wdb168728', db='work', port=3306, charset='utf8')
cursor = db.cursor()


def get_people_ids():
    sql = 'select people_id from work.people'
    cursor.execute(sql)
    ids = cursor.fetchall()
    return ids


def get_all_movie_ids_from_one_people_id(peo_id):
    sql = 'select movie_id from work.movie_people where people_id = %s'
    cursor.execute(sql, peo_id)
    movie_ids = cursor.fetchall()
    return movie_ids


def get_people_rate(movie_ids):
    sql = 'select movie_rate from work.movie_all where movie_id = %s'
    summary = 0
    num = 0
    for movie_id in movie_ids:
        cursor.execute(sql, movie_id)
        rate = cursor.fetchone()
        summary += rate[0]
        num += 1
    return summary / num


def update_people(people_id, people_rate):
    sql = 'update people set people_rate = %s where people_id = %s'
    cursor.execute(sql, (people_rate, people_id))
    db.commit()


if __name__ == '__main__':
    people_ids = get_people_ids()
    for people_id in people_ids:
        people_rate = get_people_rate(get_all_movie_ids_from_one_people_id(people_id))
        update_people(people_id, people_rate)
    print('people表的people_rates更新成功！')
    cursor.close()
    db.close()

