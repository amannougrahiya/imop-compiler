
       void combine(char *str1,char *str2)
       {
           char outstr[strlen(str1) + strlen(str2) + 2];
           strcpy(outstr,str1);
           strcat(outstr," ");
           strcat(outstr,str2);
           printf("%s\n",outstr);
}
