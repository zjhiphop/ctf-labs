import sys
import frida

def on_message(message, data):
    print("[on_message] message:", message, "data:", data)

session = frida.attach("cat" if len(sys.argv)<=1 else int(sys.argv[1]))

script = session.create_script("""
rpc.exports.enumerateModules = () => {
  return Process.enumerateModules();
};
""")
script.on("message", on_message)
script.load()

print([m["name"] for m in script.exports.enumerate_modules()])
