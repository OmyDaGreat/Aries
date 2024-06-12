import google.generativeai as genai

from Secrets import get

PROMPT = 'How are you doing?'
MODEL = 'gemini-pro'
print('** GenAI text: %r model & prompt %r\n' % (MODEL, PROMPT))

genai.configure(api_key=get('gemini'))
model = genai.GenerativeModel(MODEL)
response = model.generate_content(PROMPT)
print(response.text)
