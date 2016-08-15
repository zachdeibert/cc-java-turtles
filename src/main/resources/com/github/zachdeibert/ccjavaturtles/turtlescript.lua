id = os.getComputerID()
version = os.version()
server = "127.0.0.1"

function readConfig()
	if fs.exists("download") then
		local hnd = fs.open("download", "r")
		local content = hnd.readAll()
		hnd.close()
		local it = string.gmatch(content, "server = \".*\"")
		local line = it()
		server = string.sub(line, 7, -1)
	end
end

function registerTurtle()
	local hnd = http.post("http://" .. server .. "/register", id .. "\n" .. version)
	local resp = hnd.readAll()
	hnd.close()
	if resp == "success" then
		return true
	else
		print(resp)
		return false
	end
end

function handleCommandsApi(method, args)
	return "Not Implemented"
end

function handleDiskApi(method, args)
	return "Not Implemented"
end

function handleFsApi(method, args)
	return "Not Implemented"
end

function handleGpsApi(method, args)
	return "Not Implemented"
end

function handleIoApi(method, args)
	return "Not Implemented"
end

function handleKeysApi(method, args)
	return "Not Implemented"
end

function handleOsApi(method, args)
	return "Not Implemented"
end

function handlePaintutilsApi(method, args)
	return "Not Implemented"
end

function handlePeripheralApi(method, args)
	return "Not Implemented"
end

function handleRednetApi(method, args)
	return "Not Implemented"
end

function handleRedstoneApi(method, args)
	return "Not Implemented"
end

function handleSettingsApi(method, args)
	return "Not Implemented"
end

function handleTermApi(method, args)
	return "Not Implemented"
end

function handleTestApi(method, args)
	if method == "print" then
		print(args())
		return "OK"
	else
		return "Method not found"
	end
end

function handleTextutilsApi(method, args)
	return "Not Implemented"
end

function handleTurtleApi(method, args)
	return "Not Implemented"
end

function handleWindowApi(method, args)
	return "Not Implemented"
end

function handleEvent(event)
	local args = string.gmatch(resp, "[^,]+")
	local cmd = args()
	local parts = cmd.gmatch(resp, "[^.]+")
	local api = parts()
	local method = parts()
	if api == "commands" then
		return handleCommandsApi(method, args)
	elseif api == "disk" then
		return handleDiskApi(method, args)
	elseif api == "fs" then
		return handleFsApi(method, args)
	elseif api == "gps" then
		return handleGpsApi(method, args)
	elseif api == "io" then
		return handleIoApi(method, args)
	elseif api == "keys" then
		return handleKeysApi(method, args)
	elseif api == "os" then
		return handleOsApi(method, args)
	elseif api == "paintutils" then
		return handlePaintutilsApi(method, args)
	elseif api == "peripheral" then
		return handlePeripheralApi(method, args)
	elseif api == "rednet" then
		return handleRednetApi(method, args)
	elseif api == "redstone" then
		return handleRedstoneApi(method, args)
	elseif api == "settings" then
		return handleSettingsApi(method, args)
	elseif api == "term" then
		return handleTermApi(method, args)
	elseif api == "test" then
		return handleTestApi(method, args)
	elseif api == "textutils" then
		return handleTextutilsApi(method, args)
	elseif api == "turtle" then
		return handleTurtleApi(method, args)
	elseif api == "window" then
		return handleWindowApi(method, args)
	else
		return "API Not Found"
	end
end

function handleEvents()
	local hnd = http.post("http://" .. server .. "/pull", id)
	local resp = hnd.readAll()
	for ev in string.gmatch(resp, "[^\n]+") do
		local it = string.gmatch(ev, "[^:]+")
		local id = it()
		local event = it()
		local res = handleEvent(event)
		http.post("http://" .. server .. "/push", id .. "\n" .. event .. "\n" .. res)
	end
end

function main()
	readConfig()
	if registerTurtle() then
		while true do
			handleEvents()
		end
	end
end

main()
