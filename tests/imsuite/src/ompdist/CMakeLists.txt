cmake_minimum_required(VERSION 2.6)

project(ompdist)

file(GLOB LIB_SOURCES *.c)
file(GLOB LIB_HEADERS *.h)

add_library(ompdist ${LIB_SOURCES} ${LIB_HEADERS})
target_compile_definitions(ompdist PUBLIC CPU_USED=CPU_HASWELL)
