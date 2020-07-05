cmake_minimum_required(VERSION 2.6)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -save-temps -masm=intel -O3")

include_directories(ompdist)
add_subdirectory(ompdist)

file(GLOB EXEC_SOURCES *.c)

foreach(EXEC_SOURCE ${EXEC_SOURCES})
    get_filename_component(EXEC_NAME ${EXEC_SOURCE} NAME_WE)
    message(STATUS ${EXEC_NAME})
    add_executable(${EXEC_NAME} ${EXEC_SOURCE})
    add_definitions("-fopenmp")
    target_link_libraries(${EXEC_NAME} ompdist m omp)
endforeach(EXEC_SOURCE ${EXEC_SOURCES})
