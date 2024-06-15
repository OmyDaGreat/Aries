import google.generativeai as genai

from Keys import get

# Read the prompt from the file
with open("prompt.txt", "r") as prompt_file:
    PROMPT = prompt_file.read().strip()

MODEL = 'gemini-1.5-flash'
genai.configure(api_key=get('gemini'))
model = genai.GenerativeModel(MODEL)
response = model.generate_content(PROMPT)

with open("output.txt", "w") as output_file:
    output_file.write(response.text)

print("Response written to output.txt")
