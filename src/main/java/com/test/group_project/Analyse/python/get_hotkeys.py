# 1 连接数据库获取comment文件
# 2 切分comment并且保存到文件中
# 文件传递到reduce中进行热词总结
import json
import jieba
import jieba.analyse
import pymysql


db = pymysql.connect(host='127.0.0.1', user='root', passwd='wdb168728', db='work', port=3306, charset='utf8')
cursor = db.cursor()


def get_types():
    sql = 'select movie_type from work.type group by movie_type'
    cursor.execute(sql)
    return cursor.fetchall()


def get_movie_ids(type):
    sql = 'select movie_id from work.type where type.movie_type = %s'
    cursor.execute(sql, type)
    ids = cursor.fetchall()
    return ids


def get_comments_from_id(movie_id):
    contents_string = []
    sql = 'select comment_content from work.comment where movie_id = %s'
    cursor.execute(sql, movie_id)
    contents = cursor.fetchall()
    for content in contents:
        contents_string.append(''.join(content))
    return contents_string
    # 执行sql语句并且返回该类型所有的评论


def get_jieba_tags(contents):
    # 结巴分词获得切割
    contents_1 = ' '.join(contents)
    tags = jieba.analyse.extract_tags(contents_1, topK=50, withWeight=True)
    return tags
    # 保存到文件中供hadoop使用


# 得到某一个电影的热词
def get_movie_hotkey(movie_name):
    sql = 'select movie_id from work.movie where movie_name = %s'
    cursor.execute(sql, movie_name)
    ids = cursor.fetchall()
    tags = []
    for id in ids:
        tags.append(get_comments_from_id(id))
    return tags


def get_types_hotkey():
    types = get_types()
    for type in types:
        ids = get_movie_ids(type)
        contents = []
        for id in ids:
            contents = contents + get_comments_from_id(id)
        tags = get_jieba_tags(contents)
        json_list = []
        for tag in tags:
            dic = {}
            dic['name'] = tag[0]
            dic['value'] = tag[1]
            json_list.append(dic)
        json_str = json.dumps(json_list, ensure_ascii=False)
        with open('../../../../../../resources/static/json/'+type[0]+'.json', 'a+', newline='', encoding='utf-8') as op:
            op.write(json_str)


if __name__ == '__main__':
    # get_types_hotkey()
    get_types_hotkey()
    print('得到各个类型的热词成功！')
    cursor.close()
    db.close()
