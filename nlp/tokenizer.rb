#!/usr/bin/ruby
#
#
# Tokenizer/NLP
#
#
#

class Tokenizer
    # use delimiter to seperate the tokens.
    @@delimiter = "[^-a-zA-Z0-9']"
    def self.segment(sentence)
        sentence.gsub!(/#{@@delimiter}/, ' ')
        sentence.split(/\s+/) 
    end
end

# test case.
if __FILE__ == $0 
    Tokenizer.segment("abc edf ==a.b.c,123").each do |token|
        puts token
    end
end
