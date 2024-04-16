from pywinauto.application import Application

# Start Notepad
app = Application(backend="uia").start("notepad.exe")

# Now you can interact with Notepad, for example, type some text
app.UntitledNotepad.Edit.type_keys("Hello, this is a sample text for python automation", with_spaces=True)
