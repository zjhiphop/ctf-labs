def get_launchable_activity_aapt(self): #通过aapt找到apk的启动activity
    aapt_path = os.path.join(self.toolPath, 'aapt.exe')
    cmd = '%s dump badging "%s" ' % (aapt_path, self.apkpath)
    p = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    out,err = p.communicate()
    cmd_output = out.decode('utf-8').split('\r')
    for line in cmd_output:
        #正则，pattern.search正常，pattern.match就会有问题=-=懒得解决了
        pattern = re.compile("launchable-activity: name='(\S+)'")
        match = pattern.search(line)
        if match:
            # print match.group()[27:-1]
            return match.group()[27:-1]
       
def injectso(self):
    target_activity = self.get_launchable_activity_aapt()
    for dex in self.dexList:
        print(dex)
        if self.dexDecompile(dex):
            smali_path = os.path.join(self.decompileDir,target_activity.replace('.','\\'))+".smali"
            print(smali_path)
            with open(smali_path, 'r') as fp:
                lines = fp.readlines()
                has_clinit = False
                start = 0
                for i in range(len(lines)):  
                    #start是获取smali中，可以添加代码的位置
                    if lines[i].find(".source") != -1:
                        start = i
                    #找到初始化代码
                    if lines[i].find(".method static constructor <clinit>()V") != -1:
                        if lines[i + 3].find(".line") != -1:
                            code_line = lines[i + 3][-3:]
                            lines.insert(i + 3, "%s%s\r" % (lines[i + 3][0:-3], str(int(code_line) - 2)))
                            print("%s%s" % (lines[i + 3][0:-3], str(int(code_line) - 2)))
                            #添加相关代码
                            lines.insert(i + 4, "const-string v0, \"frida-gadget\"\r")
                            lines.insert(i + 5,
                                         "invoke-static {v0}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V\r")
                            has_clinit = True
                            break
                #如果碰上本身没有clinit函数的apk，就需要自己添加
                if not has_clinit:
                    lines.insert(start + 1, ".method static constructor <clinit>()V\r")
                    lines.insert(start + 2, ".registers 1\r")
                    lines.insert(start + 3, ".line 10\r")
                    lines.insert(start + 4, "const-string v0, \"frida-gadget\"\r")
                    lines.insert(start + 5,
                                 "invoke-static {v0}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V\r")
                    lines.insert(start + 6, "return-void\r")
                    lines.insert(start + 7, ".end method\r")

                with open(smali_path, "w") as fp:
                    fp.writelines(lines)
            self.dexCompile(dex)