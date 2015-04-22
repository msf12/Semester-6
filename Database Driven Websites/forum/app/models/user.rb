class User < ActiveRecord::Base
	acts_as_authentic
	
	has_many :posts
	has_many :forum_threads
end
