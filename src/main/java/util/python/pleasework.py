import pywinauto
from pywinauto.application import Application
import time
# Start Notepad
app = Application(backend="uia").start("Notepad.exe")

# Now you can interact with Notepad, for example, type some text
app.UntitledNotepad.Edit.type_keys("Hello, this is a sample text for python automation", with_spaces=True)

# Select "Save As" from the File menu
app.UntitledNotepad.menu_select("File->SaveAs")

# Directly interact with the "Save As" dialog by its name
# Use the child_window method with the title attribute to match the name "Save As"
time.sleep(5)
save_as_dialog = app.UntitledNotepad.child_window(title="Save As")

# Find the file name text box within the "Save As" dialog
file_name_text_box = save_as_dialog.child_window(title="File name:", control_type="Edit", found_index=0)
file_name_text_box.set_focus()

# Type the desired text into the text box
file_name_text_box.type_keys("this.txt")

# Assuming 'app' is your Application object and 'save_as_dialog' is the window object for the "Save As" dialog
save_button = save_as_dialog.child_window(best_match='Save')
save_button.click()
