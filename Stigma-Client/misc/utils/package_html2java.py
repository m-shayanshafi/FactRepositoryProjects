import os

print 'This script converts package.html files into package-info.java files'
print "Remember to run this script from root directory of project (it searches 'src' directory"

header = open('.header.txt','r')
header = header.read()

srcDir = os.path.join(os.getcwd(),'src')

print "Header\n",header

for root, directories, files in os.walk(srcDir):
        for file in files:
            if file == 'package.html':
                path = os.path.join(root, file)
                print "Found: ",path
                input = open(path, 'r')
                output = open(os.path.join(root,"package-info.java"), 'w')
                packageName = root.replace(srcDir + os.sep,'')
                packageName = packageName.replace(os.sep,'.')
                output.write(header)
                output.write('\n')
                for line in input:
                    line = line.replace('<HTML>','')
                    line = line.replace('</HTML>','')
                    line = line.replace('<html>','')
                    line = line.replace('</html>','')
                    line = line.replace('<BODY>','/** ')
                    line = line.replace('<body>','/** ')
                    line = line.replace('</BODY>',' */')
                    line = line.replace('</body>',' */')
                    output.write(line)
                output.write('\npackage ')
                output.write(packageName)
                output.write(';\n')
                output.flush()
                output.close()
                input.close()
                os.remove(path)
                    

