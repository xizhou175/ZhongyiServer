#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pandas as pd

def match_symptom( symptoms ):
	if not symptoms:
		return( {} )
	data = pd.read_excel('fangYao.xlsx')
	data[u'脉象'] = data[u'脉象'].apply(lambda x: x.split())
	data[u'中药'] = data[u'中药'].apply(lambda x: x.split())

	tmp_result = data
	for i in range( len(symptoms[u'症状']) ):
		tmp_result = tmp_result.loc[ (tmp_result[u'症状1'] == symptoms[u'症状'][i]) | (tmp_result[u'症状2'] == symptoms[u'症状'][i])
						| (tmp_result[u'症状3'] == symptoms[u'症状'][i]) | (tmp_result[u'症状4'] == symptoms[u'症状'][i])]	
	for i in range( len(symptoms[u'舌象']) ):
		tmp_result = tmp_result.loc[ tmp_result[u'舌象'] == symptoms[u'舌象'][i] ]
	for i in range( len(symptoms[u'脉象']) ):
		truth = list(tmp_result[u'脉象'].apply( lambda x: symptoms[u'脉象'][i] in x))
		tmp_result = tmp_result.loc[ truth ]
	print(len(tmp_result))
	if len(tmp_result) == 0:
		return( {} )
	elif len(tmp_result) == 1:
		result = {u'证型':list(tmp_result[u'证型'])[0],u'方剂':list(tmp_result[u'方剂'])[0],u'中药':list(tmp_result[u'中药'])[0]}
	else:
		possible_symptoms_raw = list(tmp_result[u'症状1'])+list(tmp_result[u'症状2'])+list(tmp_result[u'症状3'])+list(tmp_result[u'症状4'])
		possible_symptoms = list( set(possible_symptoms_raw) - set(symptoms[u'症状']) )
		result = {u'症状':possible_symptoms, u'舌象':list(tmp_result[u'舌象']),u'脉象':list(tmp_result[u'脉象']) }
	return(result)

symptom = {u'症状':[u"两眼干涩,视物模糊", u"肢麻关节屈伸困难"], u'脉象':[], u'舌象':[]}
print(match_symptom(symptom))