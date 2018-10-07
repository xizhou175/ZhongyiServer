# -*- coding: utf-8 -*-

import pandas as pd
import argparse
import json
import sys

def match_symptom( symptoms ):
	if not symptoms:
		return( {} )
	data = pd.read_excel('fangYao.xlsx')
	data['脉象'] = data['脉象'].apply(lambda x: x.split())
	data['中药'] = data['中药'].apply(lambda x: x.split())

	tmp_result = data
	for i in range( len(symptoms['症状']) ):
		tmp_result = tmp_result.loc[ (tmp_result['症状1'] == symptoms['症状'][i]) | (tmp_result['症状2'] == symptoms[u'症状'][i])
						| (tmp_result[u'症状3'] == symptoms[u'症状'][i]) | (tmp_result[u'症状4'] == symptoms[u'症状'][i])]	
	for i in range( len(symptoms[u'舌象']) ):
		tmp_result = tmp_result.loc[ tmp_result[u'舌象'] == symptoms[u'舌象'][i] ]
	for i in range( len(symptoms[u'脉象']) ):
		truth = list(tmp_result[u'脉象'].apply( lambda x: symptoms[u'脉象'][i] in x))
		tmp_result = tmp_result.loc[ truth ]
	if len(tmp_result) == 0:
		return( {} )
	elif len(tmp_result) == 1:
		result = {'证型':list(tmp_result[u'证型']),u'方剂':list(tmp_result[u'方剂']),u'中药':list(tmp_result[u'中药'])}
	else:
		possible_symptoms_raw = list(tmp_result['症状1'])+list(tmp_result['症状2'])+list(tmp_result['症状3'])+list(tmp_result[u'症状4'])
		possible_symptoms = list( set(possible_symptoms_raw) - set(symptoms['症状']) )
		result = {'症状':possible_symptoms, '舌象':list(tmp_result['舌象']),'脉象':list(tmp_result['脉象']) }
	return(result)

if __name__ == '__main__':
	parser = argparse.ArgumentParser()
	parser.add_argument('--symptoms')
	args = parser.parse_args()
	symptoms = args.symptoms.split(' ')
	symptom = {'症状': symptoms, '脉象': [], '舌象': []}
	#symptom = {'症状':["两眼干涩,视物模糊", "肢麻关节屈伸困难"], '脉象':[], '舌象':[]}
	mydict = match_symptom(symptom)
	sys.stdout.buffer.write(json.dumps(mydict, ensure_ascii=False).encode('utf-8'))
