import pymysql
import json
db = pymysql.connect(host='127.0.0.1', user='root', passwd='wdb168728', db='work', port=3306, charset='utf8')
cursor = db.cursor()


def write_in_file():
    sql = "select people_name,people_rate from work.people where people_type = %s order by people_rate desc limit 50"
    cursor.execute(sql, 'director')
    directors = cursor.fetchall()
    cursor.execute(sql, 'actor')
    actors = cursor.fetchall()
    Dictionary = {}
    directors_list = []
    actors_list = []
    for director in directors:
        temp_dict = {'name': director[0], 'value': director[1]}
        directors_list.append(temp_dict)
    for actor in actors:
        temp_dict = {'name': actor[0], 'value': actor[1]}
        actors_list.append(temp_dict)
    Dictionary['director'] = directors_list
    Dictionary['actor'] = actors_list
    dict_json = json.dumps(Dictionary, ensure_ascii=False)
    with open('../../../../../../resources/static/json/authors and actors.json', 'w', encoding='utf-8') as json_writer:
        json_writer.write(dict_json)


if __name__ == '__main__':
    write_in_file()
    print('将people_rates 导入文件成功！')
    cursor.close()
    db.close()
