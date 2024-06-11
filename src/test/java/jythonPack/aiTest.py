import pathlib
import textwrap

import google.generativeai as genai

from IPython.display import display
from IPython.display import Markdown

from Secrets import get


def to_markdown(text):
    text = text.replace('•', '  *')
    return Markdown(textwrap.indent(text, '> ', predicate=lambda _: True))


genai.configure(api_key=get('gemini'))
for m in genai.list_models():
    if 'generateContent' in m.supported_generation_methods:
        print(m.name)
