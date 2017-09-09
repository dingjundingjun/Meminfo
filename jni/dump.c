#include<unistd.h>
#include <jni.h>
#include<stdio.h>
#include<stdlib.h>
#include<android/log.h>
#include<elf.h>
#include<fcntl.h>
#include<android/log.h>
//#define LOG_TAG"snow"

//#defineLOGE(...)    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,__VA_ARGS__)
//使用办法
//xx pid beginaddrendaddr

JNIEXPORT jint JNICALL Java_com_example_memtest_MainActivity_getMemInfo(JNIEnv *env, jobject obj,jstring startAddr,jstring endAddr,jstring procStr) {
   char filename[256] = {0};
   const char* str_start_addr;
   const char* str_end_addr;
   const char* str_proc;
   str_start_addr = (*env)->GetStringUTFChars(env,startAddr, NULL);
   str_end_addr = (*env)->GetStringUTFChars(env,endAddr, NULL);
   str_proc = (*env)->GetStringUTFChars(env,procStr, NULL);
   long long beginaddr = strtoll(str_start_addr,NULL,16);
   long long endaddr = strtoll(str_end_addr,NULL,16);
   long long length = endaddr - beginaddr;
   //printf("-fuck-length=%x---\n",length);
   //printf("beginaddr=%x,endaddr=%x-\n",beginaddr,endaddr);
   sprintf(filename,"/proc/%s/mem",str_proc);
   printf("will open filepath=%s\n",filename);
   (*env)->ReleaseStringUTFChars(env,startAddr, str_start_addr);
   (*env)->ReleaseStringUTFChars(env,endAddr, str_end_addr);
   (*env)->ReleaseStringUTFChars(env,procStr, str_proc);
   FILE* fpsrc=fopen(filename,"r");
   FILE* fpdest=fopen("dumpMemory","wb");
   if(NULL==fpsrc||NULL==fpdest) {
       printf("open file error!!!\n");
       exit(1);
   }
   int c;
   fseek(fpsrc,beginaddr,SEEK_SET);
   while((c=fgetc(fpsrc))!=EOF&&length-->0)

   {
      fputc(c, fpdest);
   }
   fclose(fpsrc);
   fclose(fpdest);
   return 0;
}
