class BasicFiles
	attr_accessor :fileName
	attr_accessor :fileContents

	def initialize(fileName=nil)
		@fileName = fileName
	end
	
	def newFile(fileName)
		@fileName = fileName
	end

	def readFile
		@fileContents = File.readlines(Dir.pwd + "/" + fileName)
		# puts @fileContents
	end

end

class BetterFile < BasicFiles
	attr_accessor :numLines
	attr_accessor :numChars

	def initialize(fileName=nil)
		super(fileName)
	end
	
	def readFile()
		@fileContents = File.readlines(Dir.pwd + "/" + fileName)
		@numLines = 0
		@numChars = 0
		@fileContents.each do |line|
			@numLines+=1
			line.each_char {@numChars+=1}
		end
		puts "There are #{@numLines} lines and #{@numChars} characters"
	end

	def find(s=nil)
		if @fileContents.nil?
			readFile
		end
		if s.nil?
			print "String to find: "
			s = gets.chomp
		end
		# puts s
		count = 0
		locations = Array.new
		@fileContents.each_with_index do |line, i|
			x = line.index(s)
			if x
				count+=1
				# puts "String appears in line #{i+1} at character #{x}"
				locations << i
			end
		end
		puts "String appears a total of #{count} times"
		return locations
	end

	def replace(s=nil)
		if @fileContents.nil?
			readFile
		end
		if s.nil?
			print "String to find: "
			s = gets.chomp
		end
		locations = find(s)
		print "String to replace with: "
		news = gets.chomp
		unless news == ""
			locations.each do |lineNum|
				puts @fileContents[lineNum]
				@fileContents[lineNum].gsub!(s,news)
				puts @fileContents[lineNum]
			end

			print "File to save result in: "
			File.open(gets.chomp,"w") do |file|
				@fileContents.each do |line|
					file.write(line)
				end
			end
		end
		return nil
	end
	
end