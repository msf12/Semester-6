class User < ActiveRecord::Base
	validates :name, :presence => true
	validates :password, :presence => true
	
	has_many :posts
	has_many :forum_threads
end
