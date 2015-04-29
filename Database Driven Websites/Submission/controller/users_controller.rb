class UsersController < ApplicationController
	def index
		@users = User.all
	end

	def show
		if current_user
			@user = User.find(current_user)
		else
			@user = User.find(params[:id])
		end
	end

	def new
		@user = User.new
	end

	def create
		@user = User.new(user_params)

		if @user.save
    		flash[:notice] = "Registration successful."
			redirect_to root_url
		else
			render :action => 'new'
		end
	end

	def edit
		if current_user
			@user = User.find(current_user)
		else
			@user = User.find(params[:id])
		end
	end

	def update
		@user = User.find(params[:id])

		if @user.update(user_params)
			redirect_to @user
		else
			render 'edit'
		end
	end

	def destroy
		@user = User.find(params[:id])
		@user.destroy

		redirect_to users_path
	end

	private
	def user_params
		params.require(:user).permit(:username,:email,:password,:password_confirmation,:description)
	end

end
